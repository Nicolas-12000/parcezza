export interface CartResponse {
  id: number;
  items: CartItemResponse[];
  totalAmount: number;
  currency: string;
}

export interface CartItemResponse {
  id: number;
  productId: number;
  productName?: string;
  variantId: number | null;
  quantity: number;
  unitPrice: number;
  currency: string;
  lineTotal: number;
}

export interface CartItemRequest {
  productId: number;
  variantId?: number | null;
  quantity: number;
}
