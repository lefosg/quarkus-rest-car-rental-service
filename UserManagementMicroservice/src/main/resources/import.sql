delete from USERS;
delete from CHARGING_POLICIES;

-- insert customers
insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, surname, number, expirationDate, holderName) values (1000, 'ΙΩΑΝΝΗΣ','evangellou@gmail.com','johnjohn','6941603677','ΛΕΥΚΑΔΟΣ 22','ΑΘΗΝΑ','35896','166008282','Customer','ΕΥΑΓΓΕΛΟΥ','7894665213797564','2027-11-26','ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ');
insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, surname, number, expirationDate, holderName) values (1001, 'ΝΙΚΟΣ','nick7@yahoo.gr','olympiakos','6924567813','ΜΕΘΟΝΗΣ 6','ΠΕΙΡΑΙΑΣ','18545','054893175','Customer','ΠΑΠΑΔΗΜΗΤΡΙΟΥ','1645923557481658','2026-08-05','ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ');

-- insert charging policies

insert into CHARGING_POLICIES (id) values (1500);
insert into CHARGING_POLICIES (id) values (1501);

-- insert into sub tables of charging policies

insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1500, 'Glasses', 50);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1500, 'Machine', 30);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1500, 'Scratches', 90);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1500, 'Interior', 40);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1500, 'Tyres', 60);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1500, 'NoDamage', 0);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1501, 'Glasses', 30);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1501, 'Machine', 90);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1501, 'Scratches', 40);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1501, 'Interior', 50);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1501, 'Tyres', 70);
insert into POLICY_DAMAGE_COST (policy_id, damageType_key, damageType) values (1501, 'NoDamage', 0);

insert into POLICY_MILEAGE_SCALE (policy_id, mileageScale_key, mileageScale) values (1500, 100, 0.1);
insert into POLICY_MILEAGE_SCALE (policy_id, mileageScale_key, mileageScale) values (1500, 200, 0.2);
insert into POLICY_MILEAGE_SCALE (policy_id, mileageScale_key, mileageScale) values (1500, 300, 0.3);
insert into POLICY_MILEAGE_SCALE (policy_id, mileageScale_key, mileageScale) values (1501, 150, 0.15);
insert into POLICY_MILEAGE_SCALE (policy_id, mileageScale_key, mileageScale) values (1501, 250, 0.25);
insert into POLICY_MILEAGE_SCALE (policy_id, mileageScale_key, mileageScale) values (1501, 350, 0.35);

-- insert companies

insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, IBAN, income_amount, income_currency, damage_cost_amount, damage_cost_currency, policy_id) values (2000, 'AVIS','avis@gmail.com', 'password123', '2104578965','ΠΑΤΗΣΙΩΝ 37','ΑΘΗΝΑ','12478','163498317','Company','GR2514526358789654', 0, 'EUR', 0, 'EUR', 1500);
insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, IBAN, income_amount, income_currency, damage_cost_amount, damage_cost_currency, policy_id) values (2001, 'SPEED','speed@gmail.com', 'ilovecookies', '2644125415','ΛΕΥΚΩΣΙΑΣ 66','ΠΑΤΡΑ','34785','999641227','Company','GR3687254378963625', 0, 'EUR', 0, 'EUR', 1501);

