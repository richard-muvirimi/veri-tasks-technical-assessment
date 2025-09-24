# Use Node.js 18 Alpine as base image for building
FROM node:20-alpine AS build

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Production stage
FROM node:20-alpine AS production

# Set working directory
WORKDIR /app

# Copy built application from build stage
COPY --from=build /app/dist ./dist

# Copy package files for production dependencies
COPY --from=build /app/package*.json ./

# Install only production dependencies
RUN npm ci --only=production

# Expose port 4000 (default for Angular SSR)
EXPOSE 4000

# Start the SSR server
CMD ["npm", "run", "serve:ssr:fe"]
