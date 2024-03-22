FROM postgres:latest

# Environment variables
ENV DATABASE_USER=postgres
ENV DATABASE_PASSWORD=postgres
# Create the database
ENV DATABASE_DB=postgres

#hostname from databse
ENV DATABASE_HOST=postgres

#port from database
ENV DATABASE_PORT=5433

# Expose PostgreSQL port
EXPOSE 5433

# Start PostgreSQL
CMD ["postgres", "-p", "5433"]
