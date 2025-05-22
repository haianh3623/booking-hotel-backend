CREATE DATABASE HotelManagement;
USE HotelManagement;

-- Bảng User (cha)
CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    score INT DEFAULT 0,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Role
CREATE TABLE Role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng UserRole (N-N giữa User và Role)
CREATE TABLE UserRole (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE CASCADE
);

-- Bảng Hotel
CREATE TABLE Hotel (
    hotel_id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    user_id INT,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

-- Bảng Room
CREATE TABLE Room (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id INT NOT NULL,
    room_name VARCHAR(255) NOT NULL,
    area DOUBLE NOT NULL,
    combo_price2h DOUBLE NOT NULL,
    price_per_night DOUBLE NOT NULL,
    extra_hour_price DOUBLE NOT NULL,
    standard_occupancy INT NOT NULL,
    max_occupancy INT NOT NULL,
    num_children_free INT NOT NULL,
    room_img VARCHAR(255),
    bed_number INT NOT NULL,
    extra_adult BIGINT,
    description VARCHAR(500),
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hotel_id) REFERENCES Hotel(hotel_id) ON DELETE CASCADE
);

-- Bảng Room Image
CREATE TABLE RoomImage (
    img_id INT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    room_id INT,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES Room(room_id) ON DELETE CASCADE
);

-- Bảng Bill
CREATE TABLE Bill (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    total_price DOUBLE NOT NULL,
    paid_status BIT NOT NULL,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Booking
CREATE TABLE Booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    room_id INT NOT NULL,
    bill_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES Room(room_id) ON DELETE CASCADE,
    FOREIGN KEY (bill_id) REFERENCES Bill(bill_id) ON DELETE CASCADE
);

-- Bảng Review
CREATE TABLE Review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    booking_id INT NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES Booking(booking_id) ON DELETE CASCADE
);

-- Bảng Service
CREATE TABLE Service (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    service_name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL
);

-- Bảng RoomService (N-N giữa Room và Service)
CREATE TABLE RoomService (
    service_id INT NOT NULL,
    room_id INT NOT NULL,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (service_id, room_id),
    FOREIGN KEY (service_id) REFERENCES Service(service_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES Room(room_id) ON DELETE CASCADE
);

-- Bảng Notification
CREATE TABLE Notification (
    noti_id INT AUTO_INCREMENT PRIMARY KEY,
    time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);