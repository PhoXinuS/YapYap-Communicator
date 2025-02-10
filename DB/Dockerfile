FROM gvenzl/oracle-xe:latest

ENV ORACLE_ALLOW_REMOTE=true
ENV ORACLE_DISABLE_ASYNCH_IO=true
ENV ORACLE_PASSWORD=testpassword

EXPOSE 1521

COPY *.sql /docker-entrypoint-initdb.d/

CMD ["sh", "-c", "/usr/sbin/startup.sh && tail -f /dev/null"]
