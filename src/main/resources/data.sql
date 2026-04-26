INSERT INTO meeting_rooms (name, location, capacity, description, active)
VALUES
    ('Ocean', '8F A구역', 8, '소규모 미팅에 적합한 회의실입니다.', true),
    ('Forest', '8F B구역', 12, '화상 회의 장비가 포함된 중형 회의실입니다.', true),
    ('Sky', '9F 메인 홀', 20, '워크숍과 발표에 적합한 대형 회의실입니다.', true)
ON CONFLICT (name) DO NOTHING;
