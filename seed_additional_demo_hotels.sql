-- ACCOMO Hotel Reservation System
-- Idempotent Seed Script: Complete Demo Hotel Coverage
-- Adds exactly one additional distinct demo hotel for Jaipur, Bengaluru, and Kerala
-- Each hotel includes 3 distinct room categories (Standard, Deluxe, Suite) with realistic pricing.

USE hotel;

-- 1. Hotel 14: Amber Palace Heritage Resort (Jaipur)
INSERT INTO hotels (hotel_id, legacy_id, hotel_name, location, rating, price_per_night)
VALUES (14, NULL, 'Amber Palace Heritage Resort', 'Jaipur', 4.7, 15500.00)
ON DUPLICATE KEY UPDATE
    hotel_name = VALUES(hotel_name),
    location = VALUES(location),
    rating = VALUES(rating),
    price_per_night = VALUES(price_per_night);

-- 2. Hotel 15: The Garden City Boutique Hotel (Bengaluru)
INSERT INTO hotels (hotel_id, legacy_id, hotel_name, location, rating, price_per_night)
VALUES (15, NULL, 'The Garden City Boutique Hotel', 'Bengaluru', 4.8, 13500.00)
ON DUPLICATE KEY UPDATE
    hotel_name = VALUES(hotel_name),
    location = VALUES(location),
    rating = VALUES(rating),
    price_per_night = VALUES(price_per_night);

-- 3. Hotel 16: Malabar Coast Ocean & Spice Retreat (Kerala)
INSERT INTO hotels (hotel_id, legacy_id, hotel_name, location, rating, price_per_night)
VALUES (16, NULL, 'Malabar Coast Ocean & Spice Retreat', 'Kerala', 4.7, 17500.00)
ON DUPLICATE KEY UPDATE
    hotel_name = VALUES(hotel_name),
    location = VALUES(location),
    rating = VALUES(rating),
    price_per_night = VALUES(price_per_night);

-- 4. Rooms for Hotel 14 (Jaipur)
INSERT INTO rooms (room_id, hotel_id, room_type, is_available, price)
VALUES
    (40, 14, 'Standard', 1, 9500.00),
    (41, 14, 'Deluxe', 1, 15500.00),
    (42, 14, 'Suite', 1, 24000.00)
ON DUPLICATE KEY UPDATE
    hotel_id = VALUES(hotel_id),
    room_type = VALUES(room_type),
    is_available = VALUES(is_available),
    price = VALUES(price);

-- 5. Rooms for Hotel 15 (Bengaluru)
INSERT INTO rooms (room_id, hotel_id, room_type, is_available, price)
VALUES
    (43, 15, 'Standard', 1, 8500.00),
    (44, 15, 'Deluxe', 1, 13500.00),
    (45, 15, 'Suite', 1, 21000.00)
ON DUPLICATE KEY UPDATE
    hotel_id = VALUES(hotel_id),
    room_type = VALUES(room_type),
    is_available = VALUES(is_available),
    price = VALUES(price);

-- 6. Rooms for Hotel 16 (Kerala)
INSERT INTO rooms (room_id, hotel_id, room_type, is_available, price)
VALUES
    (46, 16, 'Standard', 1, 11500.00),
    (47, 16, 'Deluxe', 1, 17500.00),
    (48, 16, 'Suite', 1, 27500.00)
ON DUPLICATE KEY UPDATE
    hotel_id = VALUES(hotel_id),
    room_type = VALUES(room_type),
    is_available = VALUES(is_available),
    price = VALUES(price);
