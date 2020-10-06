ALTER TABLE enty.tb_entity DROP COLUMN template_id;
UPDATE enty.tb_client
	SET client_id=1001, client_name='xyz', client_code='FBOC', created_at=NOW(), created_by='Default User', modified_at=NOW(), modified_by='Default User', time_zone='UTC', status=true
	WHERE client_id =1001;