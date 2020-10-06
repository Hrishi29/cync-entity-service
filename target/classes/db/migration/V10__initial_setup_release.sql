ALTER TABLE enty.tb_entity RENAME COLUMN rm_id TO rm_email;
ALTER TABLE enty.tb_entity ALTER COLUMN rm_email TYPE  varchar(100) using rm_email::varchar;