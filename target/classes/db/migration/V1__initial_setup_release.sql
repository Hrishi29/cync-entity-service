CREATE SCHEMA enty;

CREATE TABLE enty.tb_address_map (
  address_map_id SERIAL NOT NULL,
  client_id bigint NOT NULL,
  city_id bigint,
  state_id bigint NOT NULL,
  zip_code bigint,
  country_id bigint NOT NULL,
  status BOOLEAN NOT NULL,
  created_by varchar(255),
  modified_by varchar(255),
  modified_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (address_map_id)
);


CREATE TABLE enty.tb_commercial_entity_details (
  business_name varchar(255) NOT NULL,
  business_type_id bigint NOT NULL,
  business_open_date DATE,
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by varchar(255),
  modified_at timestamp WITHOUT TIME ZONE,
  modified_by varchar(255) DEFAULT NULL,
  --entity_id SERIAL NOT NULL,
  entity_id varchar(20) NOT NULL,
  naics_code_id bigint,
  PRIMARY KEY (entity_id)
);

CREATE TABLE enty.tb_client (
  client_id SERIAL NOT NULL,
  client_name varchar(255) NOT NULL,
  client_code varchar(50) NOT NULL,
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by varchar(255) NOT NULL,
  modified_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_by varchar(255) NOT NULL,
  time_zone varchar(50) DEFAULT NULL,
  status BOOLEAN NOT NULL,
  PRIMARY KEY (client_id)
); 

CREATE TABLE enty.tb_configtype (
  configtype_id SERIAL NOT NULL,
  client_id bigint NOT NULL,
  configtype_code varchar(255) NOT NULL,
  configtype_name varchar(255) DEFAULT NULL,
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by varchar(255) NOT NULL,
  modified_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_by varchar(255) NOT NULL,
  PRIMARY KEY (configtype_id),
  CONSTRAINT tb_configtype_ibfk_1 FOREIGN KEY (client_id) REFERENCES enty.tb_client (client_id)
);



CREATE TABLE enty.tb_config (
  config_id SERIAL NOT NULL,
  client_id bigint NOT NULL,
  configtype_id bigint NOT NULL,
  config_code varchar(255) NOT NULL,
  config_value varchar(255) DEFAULT NULL,
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by varchar(255),
  modified_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modified_by varchar(255),
  PRIMARY KEY (config_id),
  CONSTRAINT tb_config_ibfk_1 FOREIGN KEY (configtype_id) REFERENCES enty.tb_configtype (configtype_id),
  CONSTRAINT tb_config_ibfk_2 FOREIGN KEY (client_id) REFERENCES enty.tb_client (client_id)
);

CREATE TABLE enty.tb_entity (
 -- entity_uuid varchar(50) NOT NULL UNIQUE,
  entity_type_id bigint NOT NULL,
  rm_id bigint,
  phone varchar(20),
  email varchar(100) ,
  notes varchar(255),
  map varchar(255),
  city  varchar(100),
  state_id bigint NOT NULL,
  zip_code bigint,
  country_id bigint NOT NULL,
  address varchar(255),
  created_by varchar(255) NOT NULL,
  modified_by varchar(255),
 -- entity_id SERIAL NOT NULL,
  entity_id varchar(20) NOT NULL,
  template_id bigint,
  client_id bigint NOT NULL,
  modified_at timestamp WITHOUT TIME ZONE,
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status BOOLEAN,
  PRIMARY KEY (entity_id)
);

CREATE TABLE enty.tb_entity_relationship_type_map (
  entity_relationshiptype_id SERIAL NOT NULL,
  client_id bigint NOT NULL,
  entity_id1 varchar(20) NOT NULL,
  entity_id2 varchar(20) NOT NULL,
  relationshiptype_id bigint NOT NULL,
  ownership NUMERIC (5, 2),
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_by varchar(255) NOT NULL,
  modified_at timestamp WITHOUT TIME ZONE,
  modified_by varchar(255),
  status BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (entity_relationshiptype_id)
);

CREATE TABLE enty.tb_individual_entity_details (
  date_of_birth DATE,
  first_name varchar(255) NOT NULL,
  middle_name varchar(100),
  last_name varchar(255) NOT NULL,
  created_by varchar(255) NOT NULL DEFAULT '',
  modified_by varchar(255),
  --entity_id SERIAL NOT NULL,
  entity_id varchar(20) NOT NULL,
  modified_at timestamp WITHOUT TIME ZONE,
  created_at timestamp WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (entity_id)
);