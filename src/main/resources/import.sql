delete from TECHNICAL_CHECK;
delete from RENTS;
delete from VEHICLES;
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

-- insert vehicles

insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3000, 'TOYOTA', 'YARIS', 2015, 100000, 'YMB-6325', 'Hatchback', 'Available', 30, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3001, 'VOLKSWAGEN', 'T-ROC', 2016, 80000, 'PMT-3013', 'SUV', 'Available', 50, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3002, 'RENAULT', 'MEGANE', 2018, 50000, 'KIK-2160', 'Sedan', 'Available', 40, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3003, 'MAZDA', 'MX-5', 2016, 30000, 'NAT-1234', 'Cabrio', 'Available', 50, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3004, 'MINI', 'ONE D', 2016, 240000, 'ZZE-7852', 'Mini', 'Available', 40, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3005, 'FIAT', '500 S', 2014, 92000, 'HIK-9459', 'Hatchback', 'Available', 30, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3006, 'OPEL', 'CORSA', 2018, 64000, 'PIP-4556', 'Hatchback', 'Available', 60, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3007, 'AUDI', 'A7', 2021, 100000, 'MMA-8745', 'Sedan', 'Available', 70, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3008, 'NISSAN', 'QASHQAI AUTOMATIC', 2023, 50000, 'ZIK-6834', 'SUV', 'Available', 100, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3009,'TOYOTA', 'C-HR', 2022, 49000, 'PAP-3333', 'SUV', 'Available', 80, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3010,'VOLKSWAGEN', 'POLO', 2018, 73000, 'NIK-9012', 'Hatchback', 'Available', 50, 'EUR', 0, 0, 2001);

-- insert rents

insert into RENTS(id, customer_id, vehicle_id, technical_check_id, startDate, endDate, rentState, miles, fixedCost_amount, fixedCost_currency, damageCost_amount, damageCost_currency,  totalCost_amount, totalCost_currency) values (4000, 1000, 3000, null, '2023-12-05', '2023-12-10', 'Finished', 464, 180.0, 'EUR', 50, 'EUR', 339.2000045776367, 'EUR');
insert into RENTS(id, customer_id, vehicle_id, technical_check_id, startDate, endDate, rentState, miles, fixedCost_amount, fixedCost_currency, damageCost_amount, damageCost_currency,  totalCost_amount, totalCost_currency) values (4001, 1001, 3004, null, '2023-12-15', '2023-12-25', 'Finished', 281, 440.0, 'EUR', 0, 'EUR', 494.3000030517578, 'EUR');

-- insert technical checks

insert into TECHNICAL_CHECK (id, rent_id, type, damageType) values (5000, 4000, 'Impl', 'Glasses');
insert into TECHNICAL_CHECK (id, rent_id, type, damageType) values (5001, 4001, 'Impl', 'NoDamage');

-- update rents.technical_check_id now that they have been created

update RENTS r set r.technical_check_id=5000 where r.id=4000;
update RENTS r set r.technical_check_id=5001 where r.id=4001;
