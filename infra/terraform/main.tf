terraform {
  required_providers {
    azurerm = {
      source = "hashicorp/azurerm"
      version = "4.3.0"
    }

    kubernetes = {
      source = "hashicorp/kubernetes"
      version = "2.32.0"
    }

     helm = {
      source = "hashicorp/helm"
      version = "2.15.0"
    }
  }
}

provider "azurerm" {
  # Configuration options
  use_oidc = true
  features {
    
  }
}

# provider "kubernetes" {
#   # Configuration options
# }

# provider "helm" {
#   # Configuration options
#   kubernetes {
#         host     = azurerm_kubernetes_cluster.aks.kube_config_raw.0.host
#         client_key             = base64decode(azurerm_kubernetes_cluster.your_cluster.kube_config.0.client_key)
#         client_certificate     = base64decode(azurerm_kubernetes_cluster.your_cluster.kube_config.0.client_certificate)
#         cluster_ca_certificate = base64decode(azurerm_kubernetes_cluster.your_cluster.kube_config.0.cluster_ca_certificate)
#     } 
# }

# ----- Resource Group -----

resource "azurerm_resource_group" "aks_rg" {
  name     = var.aks_rg_name
  location = var.location
}

# ----- Kubernetes Cluster (AKS) -----

resource "azurerm_kubernetes_cluster" "aks" {
  name                = var.cluster_name
  kubernetes_version  = var.kubernetes_version
  location            = azurerm_resource_group.aks_rg.location
  resource_group_name = azurerm_resource_group.aks_rg.name
  dns_prefix          = var.cluster_name

  default_node_pool {
    name       = "default"
    node_count = var.system_node_count
    vm_size    = "Standard_D2_v2"
  }

  identity {
    type = "SystemAssigned"
  }

  network_profile {
    load_balancer_sku = "standard"
    network_plugin    = "kubenet"
  }

  tags = {
    Environment = "Dev/Test"
  }
}

    # Now configure the AKS cluster
# resource "kubernetes_namespace" "ingress-nginx" {
#   metadata {
#     name = "ingress-nginx"
#   }
# }

# data "helm_repository" "stable" {
#   name = "stable"
#   url  = "https://kubernetes-charts.storage.googleapis.com"
# }

# resource "helm_release" "nginix_ingress" {
#     name      = "nginx_ingress"
#     repository = data.helm_repository.stable.metadata.0.name
#     chart     = "stable/nginx-ingress"
#     namespace = kubernetes_namespace.ingress-nginx
    
# }

# ----- Storage Account -----

resource "azurerm_resource_group" "storage" {
  name     = var.storage_rg_name
  location = var.location
}


resource "azurerm_storage_account" "storage_account" {
  name                     = var.storage_account_name
  resource_group_name      = azurerm_resource_group.storage.name
  location                 = azurerm_resource_group.storage.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  public_network_access_enabled = true
  account_kind = "BlobStorage"

  tags = {
    environment = "Dev/Test"
  }
}

resource "azurerm_storage_container" "storage_container" {
  name  =  "tfstate"
  storage_account_name = azurerm_storage_account.storage_account.name
  container_access_type = "private"
}

output "storage_container_name" {
  value = azurerm_storage_container.storage_container.name
  description = "Container name of storage account    "
}

output "storage_account_connection_string" {
  value = azurerm_storage_account.storage_account.primary_connection_string
  sensitive = true
  description = "Connection string for storage account"
}


# output "client_certificate" {
#   value     = azurerm_kubernetes_cluster.aks.kube_config[0].client_certificate
#   sensitive = true
# }

# output "kube_config" {
#   value = azurerm_kubernetes_cluster.aks.kube_config_raw

#   sensitive = true
# }