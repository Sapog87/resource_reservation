INSERT INTO public.roles (id, name) VALUES (nextval('roles_seq'),'USER');
INSERT INTO public.roles (id, name) VALUES (nextval('roles_seq'),'ADMIN');
INSERT INTO public.users (id, name, password) VALUES (nextval('users_seq'),'admin', '$2a$10$EL6aw5KxLwZlMFA7itFVlOh3h0mAMY443en/yKu77pqrlIurNOYDu');
INSERT INTO public.users_authorities (authorities_id, users_id)
SELECT 
	(SELECT id FROM public.roles WHERE name = 'ADMIN') AS authorities_id,
   	(SELECT id FROM public.users WHERE name = 'admin') AS users_id;






















