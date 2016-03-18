/*
find.permissions.with.role.powered.info.by.role.id
*/
SELECT
	p.*, CASE srp.id IS NULL
WHEN 1 THEN
	''
ELSE
	'selected'
END AS hasr_permission
FROM
	tdb_permission p
LEFT JOIN (
	SELECT
		*
	FROM
		tdb_role_permission
	WHERE
		r_id =@id
) srp ON p.id = srp.p_id
/*
find.permissions.with.user.powered.info.by.user.id
*/
SELECT
	p.*, CASE sup.id IS NULL
WHEN 1 THEN
	''
ELSE
	'selected'
END AS has_permission
FROM
	tdb_permission p
LEFT JOIN (
	SELECT
		*
	FROM
		tdb_user_permission
	WHERE
		u_id = @id
	AND u_type = @type
) sup ON p.id = sup.p_id
/*
 select.direct.permission.by.user.id
 */
SELECT
	*
FROM
	tdb_permission
WHERE
	id IN (
		SELECT DISTINCT
			p_id
		FROM
			tdb_user_permission
		WHERE
			u_id = @userId
		AND u_type = @type
	)
/*
 find.permission.by.role
 */
SELECT
	*
FROM
	tdb_permission
WHERE
	id IN (
		SELECT DISTINCT
			p_id
		FROM
			tdb_role_permission $condition
	)