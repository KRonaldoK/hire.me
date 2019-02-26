FROM mongo:4.0.6
COPY ./url_repo_db.js /docker-entrypoint-initdb.d
ENV MONGO_INITDB_DATABASE url_repo_db