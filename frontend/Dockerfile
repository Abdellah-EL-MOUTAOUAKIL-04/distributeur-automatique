# Stage 1: Build the Angular application
# Use an official Node.js LTS image (Alpine version for smaller size)
FROM node:20-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (or yarn.lock)
COPY package.json package-lock.json* ./
# If you use yarn, uncomment the line below and comment out the npm ci line
# COPY yarn.lock ./

# Install dependencies
# Using npm ci for cleaner installs, ensure you have a package-lock.json
RUN npm ci
# If you use yarn, uncomment the line below and comment out the npm ci line
# RUN yarn install --frozen-lockfile

# Copy the rest of the application code
COPY . .

# Build the application for production
# The output will be in /app/dist/frontend (assuming your project name in angular.json is 'frontend')
RUN npm run build -- --configuration production

# Stage 2: Serve the application with Nginx
# Use an official Nginx image (Alpine version for smaller size)
FROM nginx:1.25-alpine

# Remove default Nginx static assets and default config
RUN rm -rf /usr/share/nginx/html/*
RUN rm -f /etc/nginx/conf.d/default.conf

# Copy the custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy the built application from the builder stage to Nginx's web root
COPY --from=builder /app/dist/frontend/browser/ /usr/share/nginx/html/

# Expose port 80 for Nginx
EXPOSE 80

# Default command to start Nginx
CMD ["nginx", "-g", "daemon off;"]
