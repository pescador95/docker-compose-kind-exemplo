# Kubernetes

O kubectl é uma ferramenta de linha de comando utilizada para interagir com clusters Kubernetes. Com o kubectl, é possível gerenciar aplicativos, implantar contêineres, visualizar recursos e muito mais. Abaixo estão os principais comandos e funcionalidades do kubectl.

# Instalação

Para instalar o kubectl, você pode seguir as instruções fornecidas na [documentação oficial do Kubernetes.](https://kubernetes.io/pt-br/docs/tasks/tools/)

# Principais Comandos

## Obter Informações

- `kubectl cluster-info`: Exibe informações do cluster Kubernetes.

- `kubectl get pods`: Lista todos os pods em execução no cluster.

- `kubectl get services`: Lista todos os serviços disponíveis no cluster.

- `kubectl get deployments`: Lista todas as implantações no cluster.

## Manipulação de Recursos

- `kubectl create`: Cria um recurso a partir de um arquivo de configuração.
Exemplo: `kubectl create -f arquivo.yaml.`

- `kubectl apply`: Cria ou atualiza recursos com base em arquivos de configuração.
Exemplo: `kubectl apply -f arquivo.yaml.`

- `kubectl delete`: Exclui um recurso.
Exemplo: `kubectl delete pod <nome-do-pod>.`

## Visualização de Detalhes

- `kubectl describe`: Exibe detalhes de um recurso específico.
Exemplo: `kubectl describe pod <nome-do-pod>.`

- `kubectl logs`: Exibe os logs de um contêiner em um pod.
Exemplo: `kubectl logs <nome-do-pod>.`

## Execução de Comandos

- `kubectl exec`: Executa um comando em um contêiner em execução.
Exemplo: `kubectl exec -it <nome-do-pod> -- comando.`

- `kubectl run`: Cria e executa um contêiner.
Exemplo: `kubectl run <nome> --image=<imagem>.`

## Manipulação de Secrets

Criar um Secret a partir de Variáveis Literais:

- `kubectl create secret generic NOME_DO_SECRET --from-literal=chave1=valor1 --from-literal=chave2=valor2`

Excluir um Secret:

- `kubectl delete secret NOME_DO_SECRET`

## Escalonamento e Atualização

- `kubectl scale`: Altera o número de réplicas de uma implantação, conjunto de réplicas ou replicaset.
Exemplo: `kubectl scale deployment <nome-da-implantação> --replicas=<número>.`

- `kubectl rollout`: Gerencia o processo de rollout de uma atualização de implantação.
Exemplo: `kubectl rollout status deployment <nome-da-implantação>.`

## Acesso ao Cluster

- `kubectl port-forward`: Encaminha as portas de um pod localmente.
Exemplo: `kubectl port-forward <nome-do-pod> <porta-local>:<porta-remota>.`

`kubectl port-forward svc/backend-service 30110:80`


- `kubectl proxy`: Cria um proxy HTTP entre o computador local e o API Server do Kubernetes.

## Configuração

- `kubectl config`: Gerencia a configuração do kubeconfig.
Exemplo: `kubectl config view.`


[voltar](../../README.md)