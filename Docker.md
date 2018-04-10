### Docker layer
One command each layer

bootfs --> rootfs --> ...

### Dockerfile
[Official document](https://docs.docker.com/engine/reference/builder/)
[Dockerfile example](https://docs.docker.com/engine/reference/builder/#dockerfile-examples)

We can use "docker commit" or Dockerfile to create new images based on base images. However "docker commit" was deprecated, and Dockerfile is the officially recommended way.
#### Dockerfile cmds(all in capitalized case)
**FROM** The first cmd in Dockerfile, use this cmd to set which image this image was built. 
> The FROM instruction initializes a new build stage and sets the Base Image for subsequent instructions.

**MAINTAINOR**[deprecated], use LABEL in need. Information of maintaintor, e.g name and email.
**RUN** Cmd we want to run while building images. It will execute any commands in a new layer on top of the current image and commit the results.
**EXPOSE** Expose ports after the container was started with the image we built. 
> The EXPOSE instruction informs Docker that the container listens on the specified network ports at runtime.

**ADD** \<src\> \<dst\> The same with COPY, the only difference is this command will disable build cache and always fetch latest resources. 
 > The ADD instruction copies new files, directories or remote file URLs from <src> and adds them to the filesystem of the image at the path \<dest\>.
  
**COPY** Copy files(dirs) from workspace to docker images. 
**ENTRYPOINT**(exec/shell) The same with CMD, the sigificant difference is this command won't be overwritten by params that user specified while starting the container. 
> An ENTRYPOINT allows you to configure a container that will run as an executable.

**CMD** The command we want to run after the container was started, there can only one CMD command in Dockerfile, and usually at the end of the file.
**WORKDIR** Set workspace directly we want to use in Dockerfile(Somehow like "cd"). 

> The WORKDIR instruction sets the working directory for any `RUN, CMD, ENTRYPOINT, COPY` and `ADD` instructions that follow it in the Dockerfile. If the WORKDIR doesn’t exist, it will be created even if it’s not used in any subsequent Dockerfile instruction.
**ENV** Set environment variables in build stage. Use ${variable_name} syntax to get the environment variable.
**USER** 
> The USER instruction sets the user name (or UID) and optionally the user group (or GID) to use when running the image and for any RUN, CMD and ENTRYPOINT instructions that follow it in the Dockerfile. By default container was run by root.
**VOLUME** Mount file system on host server to container. Used for data persistence, data sharing and so on.
**ONBUILD** The actions we want to do while build images with this Dockerfile, this cmd can be inherited by its' son images but not grandchildren and their desendants. 
> The ONBUILD instruction adds to the image a trigger instruction to be executed at a later time, when the image is used as the base for another build. The trigger will be executed in the context of the downstream build, as if it had been inserted immediately after the FROM instruction in the downstream Dockerfile.


###Docker cmds
`docker build`
`docker run`
`docker start`
`docker stop`
`docker push`
`docker pull`
`docker images`
`docker ps [-a]`
`docker port [name] [exposed port]`
`docker inspact [name/id]`

###Docker network
####1. Docker link 
Multiple containers can communicat with each other via virtual network card **docker0**, which was created by docker server on host machine.
####1. Expose port in docker
As long as the port exposed to public network, all clients can access this port, so it's not a secure way.

