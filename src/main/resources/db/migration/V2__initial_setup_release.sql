CREATE SEQUENCE enty.tb_entity_entity_id_seq;
ALTER SEQUENCE enty.tb_entity_entity_id_seq OWNED BY enty.tb_entity.entity_id;

ALTER TABLE enty.tb_individual_entity_details
ADD CONSTRAINT constraint_fk_entity_id
FOREIGN KEY (entity_id)
REFERENCES enty.tb_entity(entity_id);

ALTER TABLE enty.tb_commercial_entity_details	
ADD CONSTRAINT constraint_fk_entity_id	
FOREIGN KEY (entity_id)
REFERENCES enty.tb_entity(entity_id);

ALTER TABLE enty.tb_entity		
ADD CONSTRAINT constraint_fk_tb_entity	
FOREIGN KEY (client_id)
REFERENCES enty.tb_client(client_id);

ALTER TABLE enty.tb_entity		
ADD CONSTRAINT constraint_fk_tb_entity_type_id	
FOREIGN KEY (entity_type_id)
REFERENCES enty.tb_config(config_id);
----------
ALTER TABLE enty.tb_entity		
ADD CONSTRAINT constraint_fk_tb_entity_state_id	
FOREIGN KEY (state_id)
REFERENCES enty.tb_config(config_id);


ALTER TABLE enty.tb_entity		
ADD CONSTRAINT constraint_fk_tb_entity_country_id	
FOREIGN KEY (country_id)
REFERENCES enty.tb_config(config_id);
---------------
ALTER TABLE enty.tb_address_map		
ADD CONSTRAINT constraint_fk_tb_address_map_client_id	
FOREIGN KEY (client_id)
REFERENCES enty.tb_client(client_id);


ALTER TABLE enty.tb_address_map		
ADD CONSTRAINT constraint_fk_tb_address_map_state_id	
FOREIGN KEY (state_id)
REFERENCES enty.tb_config(config_id);


ALTER TABLE enty.tb_address_map		
ADD CONSTRAINT constraint_fk_tb_address_map_country_id	
FOREIGN KEY (country_id)
REFERENCES enty.tb_config(config_id);

----<entity relationship type mapping constraints begin>---
ALTER TABLE enty.tb_entity_relationship_type_map		
ADD CONSTRAINT constraint_fk_tb_entity_relationship_type_map_entity_id1	
FOREIGN KEY (entity_id1)
REFERENCES enty.tb_entity(entity_id); 

ALTER TABLE enty.tb_entity_relationship_type_map		
ADD CONSTRAINT constraint_fk_tb_entity_relationship_type_map_entity_id2	
FOREIGN KEY (entity_id2)
REFERENCES enty.tb_entity(entity_id);

ALTER TABLE enty.tb_entity_relationship_type_map		
ADD CONSTRAINT constraint_fk_tb_entity_relationship_type_map_client_id	
FOREIGN KEY (client_id)
REFERENCES enty.tb_client(client_id);
 
ALTER TABLE enty.tb_entity_relationship_type_map		
ADD CONSTRAINT constraint_fk_tb_entity_relationship_type_map_entity_relationshiptype_id	
FOREIGN KEY (relationshiptype_id)
REFERENCES enty.tb_config(config_id);

----<entity relationship type mapping constraints end >---

INSERT INTO enty.tb_client (client_id, client_name, client_code,  created_by, modified_by,  time_zone, status, created_at, modified_at) 
VALUES ('1001', 'xyz', 'FBOC', 'Default User', 'Default User', 'UTC', true, NOW(), NOW());

INSERT INTO enty.tb_configtype (configtype_id, client_id,  configtype_code, configtype_name,  created_by, modified_by) 
VALUES ('2000001', '1001', 'COUNTRY', 'Country', 'Default user', 'default User');
INSERT INTO enty.tb_configtype (configtype_id, client_id,  configtype_code, configtype_name,  created_by, modified_by) 
VALUES ('2000002', '1001', 'STATE', 'State', 'Default user', 'default User');
INSERT INTO enty.tb_configtype (configtype_id, client_id,  configtype_code, configtype_name,  created_by, modified_by) 
VALUES ('2000003', '1001', 'ENTITY', 'Entity type', 'Default user', 'default User');
INSERT INTO enty.tb_configtype (configtype_id, client_id,  configtype_code, configtype_name,  created_by, modified_by) 
VALUES ('2000004', '1001', 'BUSINESS_TYPE', 'Business Type', 'Default user', 'default User');
INSERT INTO enty.tb_configtype (configtype_id, client_id,  configtype_code, configtype_name,  created_by, modified_by) 
VALUES ('2000005', '1001', 'ENTITY_RELATIONSHIP_TYPE', 'Entity Relationship Type', 'Default user', 'default User');

INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000001', '1001', '2000003', 'INDIVIDUAL', 'Individual', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000002', '1001', '2000003', 'COMMERCIAL', 'Commercial', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000003', '1001', '2000004', 'LLC', 'LLC', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000004', '1001', '2000004', 'S-Corp', 'S-Corp', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000005', '1001', '2000004', 'C-Corp', 'C-Corp', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000006', '1001', '2000004', 'Partnership', 'Partnership', 'default User','default User');

INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000007', '1001', '2000005', 'Affiliated company', 'Affiliated company', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000008', '1001', '2000005', 'Owner', 'Owner', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000009', '1001', '2000005', 'Subsidiary', 'Subsidiary', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000010', '1001', '2000005', 'Spouse', 'Spouse', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000011', '1001', '2000005', 'Owned Company', 'Owned Company', 'default User','default User');
INSERT INTO enty.tb_config (config_id, client_id,  configtype_id, config_code,  config_value, created_by, modified_by) 
VALUES ('3000012', '1001', '2000005', 'Add any other potential roles', 'Add any other potential roles', 'default User','default User');


