export type ReturnStatus = 'REQUESTED' | 'APPROVED' | 'REJECTED' | 'RECEIVED' | 'REFUNDED';

export interface ReturnResponse {
  id: number;
  orderId: number;
  status: ReturnStatus;
  reason: string;
  note?: string | null;
  createdAt: string;
}
