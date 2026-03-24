# Controller Refactoring Plan

## Overview
This document outlines the improvements to controller structure, URI patterns, and response handling.

## 1. Current Issues

### URI Inconsistencies
- Domain controllers use `/api/order-domain`, `/api/product-domain` (unnecessary `/api` prefix)
- Main controllers mix patterns: `/auth`, `/products`, `/orders/eta`
- No versioning strategy
- Redundant nested paths

### Response Handling Issues
- Direct `ResponseEntity<T>` returns without standard wrapper
- Inconsistent error handling (null checks scattered throughout)
- No metadata (timestamps, request IDs, pagination)
- Manual 404 handling in every endpoint

## 2. Proposed Solutions

### A. API Response Wrapper (✅ Created)
```java
ApiResponse<T>
├── success: boolean
├── message: String
├── data: T
├── error: ErrorDetails
├── timestamp: LocalDateTime
├── path: String
└── pagination: PaginationMetadata
```

**Benefits:**
- Consistent response format across all endpoints
- Built-in error handling
- Easy to add metadata
- Client-side handling simplified

### B. URI Standardization

#### Before vs After

**Authentication & Public Endpoints:**
- ✅ `/auth/login` - Keep as is
- ✅ `/auth/signup` - Keep as is
- ✅ `/landing` - Keep as is

**Resource Endpoints (Option 1: No versioning):**
```
Before:                          After:
/products                    →   /products
/products/{id}               →   /products/{id}
/products/search             →   /products/search
/products/category/{id}      →   /categories/{id}/products
/orders                      →   /orders
/orders/{id}                 →   /orders/{id}
/orders/eta/{orderId}        →   /orders/{id}/eta
/users/me                    →   /users/me
/warehouses                  →   /warehouses
/categories                  →   /categories
/offers                      →   /offers
/admin                       →   /admin
```

**Resource Endpoints (Option 2: With versioning - RECOMMENDED):**
```
Before:                          After:
/products                    →   /api/v1/products
/products/{id}               →   /api/v1/products/{id}
/orders                      →   /api/v1/orders
/orders/eta/{orderId}        →   /api/v1/orders/{id}/eta
/users/me                    →   /api/v1/users/me
/warehouses                  →   /api/v1/warehouses
/categories                  →   /api/v1/categories
/offers                      →   /api/v1/offers
/admin                       →   /api/v1/admin
```

**Domain Controllers (Internal APIs):**
```
Before:                                    After:
/api/order-domain/orders               →   /internal/orders
/api/product-domain/products           →   /internal/products
/api/user-domain/users                 →   /internal/users
/api/recommendation-domain/landing     →   /internal/recommendations
```

### C. Response Helper Usage

#### Before:
```java
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
    ProductDTO product = productService.getProductById(id);
    if (product != null) {
        return ResponseEntity.ok(product);
    }
    return ResponseEntity.notFound().build();
}
```

#### After:
```java
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<ProductDTO>> getProduct(@PathVariable Long id) {
    ProductDTO product = productService.getProductById(id);
    return ResponseHelper.ok(product);
}
```

Or with exception handling in service layer:
```java
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<ProductDTO>> getProduct(@PathVariable Long id) {
    return ResponseHelper.ok(productService.getProductById(id));
}
```

## 3. Implementation Steps

### Phase 1: Core Infrastructure (✅ Completed)
- [x] Create `ApiResponse<T>` wrapper
- [x] Create `ApiConstants` for path constants
- [x] Create `ResponseHelper` utility class

### Phase 2: Controller Refactoring
1. Update `ProductController`
2. Update `OrderController`
3. Update `UserController`
4. Update `CategoryController`
5. Update `SeasonalOfferController`
6. Update `WarehouseController`
7. Update `AdminController`
8. Update `ETAController`
9. Update `LandingPageController`
10. Update domain controllers (optional - internal APIs)

### Phase 3: Global Exception Handling
- Update `GlobalExceptionHandler` to use `ApiResponse`
- Handle `ResourceNotFoundException` globally
- Add validation error handling

## 4. Example Refactored Controller

```java
@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        return ResponseHelper.ok(productService.getAllActiveProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        return ResponseHelper.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return ResponseHelper.created(productService.getProductById(created.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.disableProduct(id);
        return ResponseHelper.noContent();
    }
}
```

## 5. Breaking Changes Warning

⚠️ **IMPORTANT:** These changes will modify the API contract.

**Response format changes from:**
```json
{
  "id": 1,
  "name": "Product Name"
}
```

**To:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "Product Name"
  },
  "timestamp": "2025-01-08T10:30:00"
}
```

**Migration Strategy:**
1. Create new versioned endpoints (`/api/v1/*`)
2. Keep old endpoints for backward compatibility
3. Deprecate old endpoints with warnings
4. Remove after grace period

**OR (simpler for internal project):**
- Update all endpoints at once
- Update frontend/mobile clients simultaneously
- Document changes in API documentation

## 6. Benefits Summary

✅ **Consistency:** All endpoints return the same structure
✅ **Maintainability:** Less boilerplate, centralized logic
✅ **Error Handling:** Standardized error responses
✅ **Metadata:** Built-in support for pagination, timestamps
✅ **Client-Friendly:** Easier to handle responses on frontend
✅ **Future-Proof:** Easy to add new features (correlation IDs, etc.)
✅ **API Versioning:** Prepared for future changes

## 7. Recommendation

**RECOMMENDED APPROACH:**
1. Apply response wrapper to all controllers
2. Add `/api/v1` prefix to all resource endpoints
3. Keep `/auth` and `/landing` without versioning (public endpoints)
4. Rename domain controllers to `/internal/*` (if they're internal-only)
5. Update frontend clients to handle new response format
6. Update API documentation (Swagger/OpenAPI)

This approach provides the best balance between:
- Backward compatibility (through versioning)
- Consistency (standard response format)
- Maintainability (less boilerplate)
- Future extensibility (easy to add v2, v3)
