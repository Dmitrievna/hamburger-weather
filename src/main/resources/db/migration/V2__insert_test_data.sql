-- V2__insert_test_data.sql
INSERT INTO route_requests (tag, start_street, start_house_number, start_plz, start_city, start_country, end_street, end_house_number, end_plz, end_city, end_country, coordinates, requested_at)
VALUES (
    'home-to-work',
    'Musterstraße', '1', '20099', 'Hamburg', 'Germany',
    'Reeperbahn', '1', '20099', 'Hamburg', 'Germany',
    '[{"latitude": 53.550341, "longitude": 10.000654}]',
    NOW()
);