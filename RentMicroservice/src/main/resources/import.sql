delete from TECHNICAL_CHECK;
delete from RENTS;


-- insert rents

insert into RENTS(id, customerId, vehicleId, technical_check_id, startDate, endDate, rentState, miles, fixedCost_amount, fixedCost_currency, damageCost_amount, damageCost_currency, mileageCost_amount, mileageCost_currency, totalCost_amount, totalCost_currency) values (4000, 1000, 3000, null, '2023-12-05', '2023-12-10', 'Finished', 464, 180.0, 'EUR', 50, 'EUR', 109.2, 'EUR',  339.2000045776367, 'EUR');
insert into RENTS(id, customerId, vehicleId, technical_check_id, startDate, endDate, rentState, miles, fixedCost_amount, fixedCost_currency, damageCost_amount, damageCost_currency, mileageCost_amount, mileageCost_currency, totalCost_amount, totalCost_currency) values (4001, 1001, 3004, null, '2023-12-15', '2023-12-25', 'Finished', 281, 440.0, 'EUR', 0, 'EUR', 68.35, 'EUR', 494.3000030517578, 'EUR');

-- insert technical checks

insert into TECHNICAL_CHECK (id, rent_id, type, damageType) values (5000, 4000, 'Impl', 'Glasses');
insert into TECHNICAL_CHECK (id, rent_id, type, damageType) values (5001, 4001, 'Impl', 'NoDamage');

-- update rents.technical_check_id now that they have been created

update RENTS r set r.technical_check_id=5000 where r.id=4000;
update RENTS r set r.technical_check_id=5001 where r.id=4001;
