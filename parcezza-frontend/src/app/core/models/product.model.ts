export interface Product {
  id: number;
  sku: string;
  name: string;
  description: string;
  basePrice: number;
  stock: number;
  currency: string;
  active: boolean;
  sellerId: number;
}

export interface ProductVariant {
  id: number;
  sku: string;
  priceOverride: number | null;
  stock: number;
  attributes: VariantAttribute[];
}

export interface VariantAttribute {
  key: string;
  value: string;
}
