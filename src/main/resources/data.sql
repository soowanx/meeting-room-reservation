INSERT INTO meeting_rooms (name, location, capacity, description, room_type, region, active)
VALUES
    ('Ocean', '8F A구역', 8, '소규모 미팅에 적합한 회의실입니다.', 'MEETING_ROOM', '역삼·선릉', true),
    ('Forest', '8F B구역', 12, '화상 회의 장비가 포함된 중형 회의실입니다.', 'MEETING_ROOM', '강남·신논현', true),
    ('Sky', '9F 메인 홀', 20, '워크숍과 발표에 적합한 대형 회의실입니다.', 'SEMINAR_EVENT_HALL', '삼성·코엑스', true),
    ('Vision', '10F IR존', 10, '투자자 미팅과 발표에 적합한 IR룸입니다.', 'IR_ROOM', '광화문·시청·을지로', true),
    ('Studio One', '7F 스튜디오', 6, '촬영과 녹음에 적합한 스튜디오 공간입니다.', 'STUDIO', '성수·왕십리', true),
    ('Bridge', '4F 미팅존', 14, '팀 협업과 인터뷰에 적합한 미팅룸입니다.', 'MEETING_ROOM', '여의도·영등포', true),
    ('Summit', '12F 컨퍼런스존', 24, '행사와 대형 발표를 위한 이벤트홀입니다.', 'SEMINAR_EVENT_HALL', '잠실', true),
    ('Harbor', '6F IR라운지', 9, '소규모 투자 미팅에 적합한 IR룸입니다.', 'IR_ROOM', '분당', true),
    ('Canvas', '3F 크리에이티브존', 5, '촬영과 팟캐스트 녹음에 적합한 스튜디오입니다.', 'STUDIO', '홍대·공덕', true),
    ('Maple', '5F 포커스존', 7, '조용한 고객 미팅에 적합한 미팅룸입니다.', 'MEETING_ROOM', '방배', true),
    ('Avenue', '11F 세미나존', 18, '교육과 외부 행사에 적합한 세미나룸입니다.', 'SEMINAR_EVENT_HALL', '동대문', true),
    ('Nexus', '8F 라운지', 11, '스타트업 IR과 리뷰 세션에 적합한 공간입니다.', 'IR_ROOM', '마들', true),
    ('Pulse', '5F 브레인스토밍존', 16, '팀 워크숍과 아이데이션에 적합한 미팅룸입니다.', 'MEETING_ROOM', '잠실', true),
    ('Quartz', '13F 프레젠테이션존', 22, '고객 발표와 교육 세션에 적합한 세미나룸입니다.', 'SEMINAR_EVENT_HALL', '강남·신논현', true),
    ('River', '2F 인터뷰존', 6, '1:1 인터뷰와 소규모 미팅에 적합한 공간입니다.', 'MEETING_ROOM', '성수·왕십리', true),
    ('Signal', '9F IR스위트', 8, 'IR 피칭과 투자자 미팅에 적합한 전용룸입니다.', 'IR_ROOM', '여의도·영등포', true),
    ('Terrace', '15F 루프 라운지', 10, '브랜드 촬영과 라이브 스트리밍에 적합한 스튜디오입니다.', 'STUDIO', '분당', true),
    ('Unity Hall', '14F 컨벤션존', 28, '사내 행사와 외부 세미나 진행에 적합한 이벤트홀입니다.', 'SEMINAR_EVENT_HALL', '광화문·시청·을지로', true)
ON CONFLICT (name) DO NOTHING;

UPDATE meeting_rooms SET room_type = 'MEETING_ROOM', region = '역삼·선릉' WHERE name = 'Ocean';
UPDATE meeting_rooms SET room_type = 'MEETING_ROOM', region = '강남·신논현' WHERE name = 'Forest';
UPDATE meeting_rooms SET room_type = 'SEMINAR_EVENT_HALL', region = '삼성·코엑스' WHERE name = 'Sky';
UPDATE meeting_rooms SET room_type = 'IR_ROOM', region = '광화문·시청·을지로' WHERE name = 'Vision';
UPDATE meeting_rooms SET room_type = 'STUDIO', region = '성수·왕십리' WHERE name = 'Studio One';
UPDATE meeting_rooms SET room_type = 'MEETING_ROOM', region = '여의도·영등포' WHERE name = 'Bridge';
UPDATE meeting_rooms SET room_type = 'SEMINAR_EVENT_HALL', region = '잠실' WHERE name = 'Summit';
UPDATE meeting_rooms SET room_type = 'IR_ROOM', region = '분당' WHERE name = 'Harbor';
UPDATE meeting_rooms SET room_type = 'STUDIO', region = '홍대·공덕' WHERE name = 'Canvas';
UPDATE meeting_rooms SET room_type = 'MEETING_ROOM', region = '방배' WHERE name = 'Maple';
UPDATE meeting_rooms SET room_type = 'SEMINAR_EVENT_HALL', region = '동대문' WHERE name = 'Avenue';
UPDATE meeting_rooms SET room_type = 'IR_ROOM', region = '마들' WHERE name = 'Nexus';
UPDATE meeting_rooms SET room_type = 'MEETING_ROOM', region = '잠실' WHERE name = 'Pulse';
UPDATE meeting_rooms SET room_type = 'SEMINAR_EVENT_HALL', region = '강남·신논현' WHERE name = 'Quartz';
UPDATE meeting_rooms SET room_type = 'MEETING_ROOM', region = '성수·왕십리' WHERE name = 'River';
UPDATE meeting_rooms SET room_type = 'IR_ROOM', region = '여의도·영등포' WHERE name = 'Signal';
UPDATE meeting_rooms SET room_type = 'STUDIO', region = '분당' WHERE name = 'Terrace';
UPDATE meeting_rooms SET room_type = 'SEMINAR_EVENT_HALL', region = '광화문·시청·을지로' WHERE name = 'Unity Hall';
