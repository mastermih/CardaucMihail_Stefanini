FROM nginx:alpine

# Copy the custom nginx configuration to the container I think i do not need it here i use it in compose


EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
