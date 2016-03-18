/*
 find.role.by.user.id
 */
SELECT
	*
FROM
	tdb_role
WHERE
	id IN (
		SELECT DISTINCT
			r_id
		FROM
			tdb_user_role
		WHERE
			u_id = @userid
		AND u_type = @type
	)
/*
find.users.by.role.name
*/
SELECT
	u.*
FROM
	tdb_user u
LEFT JOIN tdb_user_role ur ON u.id = ur.u_id
LEFT JOIN tdb_role r ON ur.r_id = r.id
WHERE
	r.r_name = @name