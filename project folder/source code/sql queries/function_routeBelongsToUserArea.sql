-- checks if the given route is inside the user area
CREATE schema SocialRouting
CREATE OR REPLACE FUNCTION SocialRouting.routeBelongsToUserArea(lower_bound_radius real, upper_bound_radius real, user_latitude real, user_longitude real, route_points jsonb, circular boolean, ordered boolean)
RETURNS boolean AS $$
DECLARE
	user_point GEOGRAPHY := ST_GeographyFromText(CONCAT('Point(', CAST(user_latitude as text), ' ',CAST(user_longitude as text), ')'));
	json_point jsonb;
	current_distance real := -1;
	current_point GEOGRAPHY;
	formatted_points jsonb := ((route_points->>0)::jsonb);
	points_length integer := jsonb_array_length(formatted_points); 
	text_point text;
BEGIN
	--if circular any point inside radius
		--return true
	--else return false													
	IF circular = TRUE
	THEN													
		FOR json_point IN SELECT * FROM json_array_elements(formatted_points)
		  LOOP
			text_point = CONCAT('Point(', json_point->>'latitude', ' ', json_point->>'longitude', ')');		  
			current_point = ST_GeographyFromText(text_point);
			current_distance = ST_Distance(user_point, current_point);

			IF current_distance > lower_bound_radius AND current_distance < upper_bound_radius 
			THEN RETURN TRUE;
			END IF;

		  END LOOP;
		RETURN FALSE;
	END IF;
														
	--if unordered check last
		-- return true if yes
	if ordered = FALSE
	THEN
		text_point = CONCAT('Point(', (formatted_points-> (points_length - 1))->>'latitude', ' ', (formatted_points-> (points_length - 1))->>'longitude', ')');
		current_point = ST_GeographyFromText();	
		current_distance = ST_Distance(user_point, current_point);
		IF current_distance > lower_bound_radius AND current_distance < upper_bound_radius 
		THEN RETURN TRUE;
		END IF;
	END IF;
														
	--calculate first
		--return true if yes
	if ordered = TRUE
	THEN
		text_point = CONCAT('Point(', (formatted_points-> 0)->>'latitude', ' ', (formatted_points-> 0)->>'longitude', ')');
		current_point = ST_GeographyFromText(text_point);
		current_distance = ST_Distance(user_point, current_point);
		IF current_distance > lower_bound_radius AND current_distance < upper_bound_radius 
		THEN RETURN TRUE;
		END IF;
	END IF;
														
	RETURN FALSE;
END
$$ LANGUAGE plpgsql;
														
-- drop function routebelongstouserarea														
														
														
														
														
														
														
														