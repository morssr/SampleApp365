package test.com.sampleapp.mor.data.api.resposes

data class PropertyResponse(
    val address: String,
    val createdOn: String,
    val occupiedStats: String,
    val owner: String,
    val ownerStatus: String,
    val plan: String,
    val propertyId: String,
    val tenant: TenantResponse?
)