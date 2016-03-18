/*
find.roles.with.user.powerd.info.by.user.id
*/
SELECT
	r.*, CASE sur.id IS NULL
WHEN 1 THEN
	''
ELSE
	'selected'
END AS has_role
FROM
	tdb_role r
LEFT JOIN (
	SELECT
		*
	FROM
		tdb_user_role
	WHERE
		u_id = @id
	AND u_type = @type
) sur ON r.id = sur.r_id