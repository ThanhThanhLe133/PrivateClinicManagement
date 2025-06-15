from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import mysql.connector
from datetime import date, datetime, timedelta, time

app = FastAPI()

class SuggestAppointmentRequest(BaseModel):
    patient_id: str
    service_ids: List[str]
    urgency_level: Optional[int] = 1
    is_followup: Optional[bool] = False

class SuggestionItem(BaseModel):
    doctor_id: str
    doctor_name: str
    slot_time: datetime
    priority_score: int
    service_id: str
    service_name: str

@app.post("/suggest_appointments", response_model=List[SuggestionItem])
def suggest_appointments(req: SuggestAppointmentRequest):
    connection = None
    cursor = None
    try:
        connection = mysql.connector.connect(
            host="localhost",
            user="root",
	    	password="22792515",
            database="clinic",
	   	    #port = 3306
            port=3060,
            use_pure=True
        )
        cursor = connection.cursor(dictionary=True)

        cursor.execute("SELECT Date_of_birth FROM PATIENT WHERE Patient_id = %s", (req.patient_id,))
        patient = cursor.fetchone()
        if not patient:
            raise HTTPException(status_code=404, detail="Patient not found")

        dob = patient["Date_of_birth"]
        today = date.today()
        age = today.year - dob.year - ((today.month, today.day) < (dob.month, dob.day))

        results = []
        booked_slots = set()  # Set để lưu các slot_time đã được chọn, tránh trùng lặp

        for service_id in req.service_ids:
            cursor.execute("SELECT Name FROM SERVICE WHERE Id = %s", (service_id,))
            service = cursor.fetchone()
            if not service:
                continue
            service_name = service["Name"]

            cursor.execute("""
                SELECT d.Doctor_id, ua.Name AS Doctor_name
                FROM DOCTOR d
                JOIN USER_ACCOUNT ua ON d.Doctor_id = ua.Id
                JOIN SERVICE s ON d.Service_id = s.Id
                WHERE s.Id = %s AND d.Is_confirmed = TRUE
            """, (service_id,))
            doctors = cursor.fetchall()
            if not doctors:
                continue

            priority_score = req.urgency_level + (1 if age > 60 else 0) + (1 if req.is_followup else 0)

            suggested_slot = None
            suggested_doctor_id = None
            suggested_doctor_name = None
            earliest_time = None

            # Thay vì chỉ lấy slot sớm nhất, ta có thể tìm slot đầu tiên chưa bị trùng
            for d in doctors:
                doctor_id = d["Doctor_id"]
                doctor_name = d["Doctor_name"]

                cursor.execute("""
                    SELECT Slot_date, Slot_time FROM AVAILABLE_SLOT
                    WHERE Doctor_id = %s AND Is_booked = FALSE AND
                          CONCAT(Slot_date, ' ', Slot_time) > NOW()
                    ORDER BY Slot_date, Slot_time ASC
                """, (doctor_id,))
                slots = cursor.fetchall()

                for slot in slots:
                    slot_date = slot["Slot_date"]
                    slot_time = slot["Slot_time"]

                    if isinstance(slot_time, timedelta):
                        total_seconds = slot_time.total_seconds()
                        hours = int(total_seconds // 3600)
                        minutes = int((total_seconds % 3600) // 60)
                        seconds = int(total_seconds % 60)
                        slot_time = time(hour=hours, minute=minutes, second=seconds)

                    slot_datetime = datetime.combine(slot_date, slot_time)

                    # --- Kiểm tra bệnh nhân có lịch trùng slot chưa ---
                    cursor.execute("""
                        SELECT COUNT(*) AS count FROM APPOINTMENT a
                        WHERE a.Patient_id = %s  AND a.time = %s AND a.Status = 'coming'
                    """, (req.patient_id, slot_datetime))
                    patient_slot_count = cursor.fetchone()["count"]
                    if patient_slot_count > 0:
                        # Bệnh nhân đã có lịch trùng slot, bỏ qua slot này
                        continue

                    # Nếu slot chưa bị bác sĩ hoặc bệnh nhân đặt
                    if slot_datetime not in booked_slots:
                        if earliest_time is None or slot_datetime < earliest_time:
                            earliest_time = slot_datetime
                            suggested_slot = slot_datetime
                            suggested_doctor_id = doctor_id
                            suggested_doctor_name = doctor_name
                        break  # Tìm được slot phù hợp cho bác sĩ này thì dừng, không cần xét tiếp

            if suggested_doctor_id and suggested_slot:
                booked_slots.add(suggested_slot)  # Đánh dấu slot này đã được chọn
                results.append({
                    "doctor_id": suggested_doctor_id,
                    "doctor_name": suggested_doctor_name,
                    "slot_time": suggested_slot,
                    "priority_score": priority_score,
                    "service_id": service_id,
                    "service_name": service_name
                })

        if not results:
            raise HTTPException(status_code=404, detail="No available slots found")

        return results

    except mysql.connector.Error as e:
        raise HTTPException(status_code=500, detail=f"Database error: {e}")

    finally:
        if cursor:
            cursor.close()
        if connection:
            connection.close()
