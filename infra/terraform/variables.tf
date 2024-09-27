variable "aks_rg_name" {
    type = string
    description = "Name of the resource group containing the k8s cluster"
}

variable "location" {
    type = string
    description = "Azure regional location"
}

variable "cluster_name" {
    type = string
    description = "AKS cluster name"
}

variable "kubernetes_version" {
    type = string
    description = "Kubernetes version"
}

variable "system_node_count" {
    type = number
    description = "Node count in aks cluster"
}


variable "storage_rg_name" {
    type = string
    description = "Name of the resource group for storage account"
}


variable "storage_account_name" {
    type = string
    description = "Storage account name"
}