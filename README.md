Crear Base de datos MySQl: nombre = fondos_db
Inyectar datos a la tabla fund:

INSERT INTO fund (name, min_investment_amount, category)
VALUES 
('FPV_BTG_PACTUAL_RECAUDADORA', 75000, 'FPV'),
('FPV_BTG_PACTUAL_ECOPETROL', 125000, 'FPV'),
('DEUDAPRIVADA', 50000, 'FIC'),
('FDO-ACCIONES', 250000, 'FIC'),
('FPV_BTG_PACTUAL_DINAMICA', 100000, 'FPV');

La configuracion centralizada tiene los datos de las variables de entorno
