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
  productName?: string;
  variantId: number | null;
  quantity: number;
  unitPrice: number;
  currency: string;
  lineTotal: number;
}

export interface CheckoutRequest {
  shippingAddressId: number;
}

export type OrderStatus = 'CREATED' | 'PENDING' | 'PAID' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'REFUNDED';
export type ShipmentStatus = 'PENDING' | 'SHIPPED' | 'IN_TRANSIT' | 'DELIVERED' | 'RETURNED';
