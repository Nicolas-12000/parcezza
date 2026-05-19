export interface OrderResponse {
  id: number;
  status: OrderStatus;
  totalAmount: number;
  currency: string;
  shipmentStatus: ShipmentStatus;
  trackingCode: string;
  createdAt: string;
  items: OrderItemResponse[];
}

export interface OrderItemResponse {
  id: number;
  productId: number;
  variantId: number | null;
  quantity: number;
  unitPrice: number;
  currency: string;
  lineTotal: number;
}

export interface CheckoutRequest {
  shippingAddressId: number;
}

export type OrderStatus = 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'REFUNDED';
export type ShipmentStatus = 'PENDING' | 'SHIPPED' | 'IN_TRANSIT' | 'DELIVERED' | 'RETURNED';
