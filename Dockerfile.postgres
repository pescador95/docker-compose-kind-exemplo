FROM postgres:latest

# Environment variables
ENV DATABASE_USER=quarkusapp
ENV DATABASE_PASSWORD=quarkusapp
ENV POSTGRES_USER=quarkusapp
ENV POSTGRES_PASSWORD=quarkusapp
ENV POSTGRES_DB=quarkusapp
# Create the database
ENV DATABASE_DB=quarkusapp

#hostname from databse
ENV DATABASE_HOST=quarkusapp

#port from database
ENV DATABASE_PORT=5433

# Expose PostgreSQL port
EXPOSE 5433

# Start PostgreSQL
CMD ["postgres", "-p", "5433"]
