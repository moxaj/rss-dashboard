# stuff

## client

- token
- id->channel cache
- id->item cache

## interface

Op | Input | Output
--- | --- | ---
login | login info | token + layout
getChannel | token + channel id | channel
getItem | token + item id | item
getDashboard | token + filter | dashboard

