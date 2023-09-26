#!/bin/sh
: "${APP?Need to set APP}"

if  [[ $APP != *-pr* ]] ;
then
    echo "Not a PR"
    exit 1
fi

if [[ ! -v STAGING_DATABASE_URL ]]; then
    echo "STAGING_DATABASE_URL is not set"
    exit 1
fi

psql -t -c "select 'drop table if exists \"' || tablename || '\" cascade;'
        from pg_tables
       where schemaname = 'public'; " $DATABASE_URL | psql $DATABASE_URL

pg_dump -x -O --if-exists --clean $STAGING_DATABASE_URL | psql $DATABASE_URL