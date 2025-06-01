Mở thư mục cài đặt XAMPP, thường là C:\xampp\mysql\bin\my.ini (trên Windows).
Mở file my.ini
--sửa max_allowed_packet=128M
--Thêm dòng này dưới [mysqld] : event_scheduler=ON


cài đặt fastAPI và Uvicorn
------pip install fastapi uvicorn

cài mysql connector cho python
----------pip install mysql-connector-python


chạy trong terminal với
----------uvicorn app:app --reload
