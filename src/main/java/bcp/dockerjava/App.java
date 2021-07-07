package bcp.dockerjava;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

/**
 * Hello world!
 *
 */
public class App {

  public static void main(String[] args) {

    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("tcp://localhost:1275").build();
    DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();

    List<Network> networks = dockerClient.listNetworksCmd().withNameFilter("ETH").exec();
    if (networks.isEmpty()) {
      CreateNetworkResponse networkResponse = dockerClient
          .createNetworkCmd()
          .withName("ETH")
          .withAttachable(true)
          .withDriver("bridge").exec();
      System.out.printf("Network %s created...\n", networkResponse.getId());
    }

    CreateContainerResponse container = dockerClient
        .createContainerCmd("ubuntu")
        .withName("my-ubuntu")
        .withCmd("sleep", "100")
        .withHostConfig(HostConfig
            .newHostConfig()
            .withNetworkMode("ETH")
            .withAutoRemove(true))
        .exec();
    
    String containerId = container.getId();
    dockerClient.startContainerCmd(containerId).exec();

    Network ethNetwork = dockerClient.inspectNetworkCmd()
        .withNetworkId("ETH")
        .exec();
    
    Set<String> containerIds = ethNetwork.getContainers().keySet();
    if(containerIds.contains(containerId)) {
      System.out.printf("Container with id:%s is connected to network %s%n", containerId, ethNetwork.getName());
    }
    
    
    
    List<Container> containerList = dockerClient.listContainersCmd().withIdFilter(containerIds).exec();
    containerList.stream().forEach(c -> System.out.println(Arrays.asList(c.getNames())));

  }
}
