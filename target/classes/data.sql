INSERT INTO users(name,email,password,role) VALUES
('System Admin','admin@smartservice.com','{noop}admin123','ADMIN'),
('Rahul Sharma','employee@smartservice.com','{noop}employee123','EMPLOYEE'),
('Jagruti Salunkhe','customer@smartservice.com','{noop}customer123','CUSTOMER');
INSERT INTO tickets(ticket_number,title,description,category,priority,status,customer_name,customer_email,assigned_employee,created_at,updated_at) VALUES
('TKT-1001','Unable to login','Customer cannot access the portal.','Technical','HIGH','ASSIGNED','Jagruti Salunkhe','customer@smartservice.com','employee@smartservice.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('TKT-1002','Payment verification issue','Payment succeeded but order is pending.','Payment','CRITICAL','IN_PROGRESS','Jagruti Salunkhe','customer@smartservice.com','employee@smartservice.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('TKT-1003','Update customer profile','Customer requested profile update.','Account','LOW','RESOLVED','Jagruti Salunkhe','customer@smartservice.com','employee@smartservice.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
