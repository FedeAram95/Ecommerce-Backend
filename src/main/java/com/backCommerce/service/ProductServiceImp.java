package com.backCommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.backCommerce.dto.ProductDto;
import com.backCommerce.model.Product;
import com.backCommerce.repository.CategoryRepository;
import com.backCommerce.repository.ProductRepository;
import com.backCommerce.repository.TypesRepository;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    TypesRepository typesRepository;

    @Override
    public ResponseEntity<List<ProductDto>> getProduct(String keyword, int page, int size, Long categoryId, Long typeId) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Product> filteredProducts;


            if (keyword != null && !keyword.isEmpty()) {
                filteredProducts = productRepository.findByKeyword(keyword);
            } else {
                filteredProducts = productRepository.findAll();
            }


            if (categoryId != null) {
                filteredProducts = filteredProducts.stream()
                        .filter(p -> p.getType().equals(categoryId))
                        .toList();
            }
            if (typeId != null) {
                filteredProducts = filteredProducts.stream()
                        .filter(p -> p.getType() != null && p.getType().getId().equals(typeId))
                        .toList();
            }

            int start = Math.min((int) pageable.getOffset(), filteredProducts.size());
            int end = Math.min((start + pageable.getPageSize()), filteredProducts.size());
            List<ProductDto> result = filteredProducts.subList(start, end).stream()
                    .map(this::convertToDTO)
                    .toList();

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error al obtener productos filtrados: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity getProductById(Long id) {
        try {
            ProductDto product = productRepository.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
            if (product != null) {
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("producto no encontrado, id inexistente",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        try {
            Product newProduct = new Product();

            newProduct.setName(productDto.getName());
            newProduct.setDescription(productDto.getDescription());
            newProduct.setPrice(productDto.getPrice());
            newProduct.setStock(productDto.getStock());
            newProduct.setImage(productDto.getImage());

            if(productDto.getCategoryId() != null) {
            	newProduct.setType(typesRepository.findById(productDto.getCategoryId()).get());
            }
            // Guardar el nuevo producto
            productRepository.save(newProduct);
            return new ResponseEntity<>(convertToDTO(newProduct), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                productRepository.deleteById(id);
                return new ResponseEntity<>("Producto eliminado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ProductDto> updateProduct(Long productId, ProductDto product) {
        try {
            Product prod = convertToEntity(product);
            Optional<Product> existingProduct = productRepository.findById(productId);
            if (existingProduct.isPresent()) {
                Product updatedProduct = existingProduct.get();
                updatedProduct.setName(prod.getName());
                updatedProduct.setDescription(prod.getDescription());
                updatedProduct.setStock(prod.getStock());
                updatedProduct.setPrice(prod.getPrice());
                if(product.getCategoryId() != null) {
                    updatedProduct.setType(typesRepository.findById(product.getCategoryId()).get());
                }
                String tags = String.join(",", prod.getTags());
                updatedProduct.setTags(tags);
                if(product.getImage() != null) {
                	updatedProduct.setImage(product.getImage()); // Actualiza la imagen en el producto existente
                }

                productRepository.save(updatedProduct);
                return new ResponseEntity<>(convertToDTO(updatedProduct), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setStock(productDto.getStock());
        product.setPrice(productDto.getPrice());

        product.setImage(productDto.getImage());
        product.setTags(productDto.getTags());
        return product;
    }

    private ProductDto convertToDTO(Product product) {
        ProductDto productDTO = new ProductDto();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setStock(product.getStock());
        productDTO.setPrice(product.getPrice());
        productDTO.setTypeId(product.getType() != null ? product.getType().getId() : null);
        productDTO.setImage(product.getImage());
        productDTO.setTags(product.getTags());
        productDTO.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        productDTO.setCategoryName(product.getType() != null ? product.getType().getDescription() : null);
        return productDTO;
    }




}
