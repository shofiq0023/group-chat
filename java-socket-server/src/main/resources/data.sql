INSERT INTO public.groups (
    id,
    has_member_limitations,
    max_group_members,
    group_code,
    group_name,
    group_image_link,
    group_password,
    group_privacy,
    created_at,
    modified_at
)
VALUES (
           1,
           false,
           1,
           'BRDCST',
           'Broadcast To All',
           null,
           null,
           'PUBLIC',
           NOW(),
           NOW()
       )
ON CONFLICT (group_code) DO NOTHING;