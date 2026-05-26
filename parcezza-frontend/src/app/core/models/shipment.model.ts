export type ShipmentStatus = 'PENDING' | 'SHIPPED' | 'IN_TRANSIT' | 'DELIVERED' | 'RETURNED' | 'CANCELLED';

export interface ShipmentResponse {
  id: number;
  orderId: number;
  status: ShipmentStatus;
  trackingCode?: string | null;
}
