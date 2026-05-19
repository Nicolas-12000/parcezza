export interface PaymentRequest {
  orderId: number;
  provider: string;
  providerRef?: string;
  cardNumber: string;
  cardHolder: string;
  expMonth: number;
  expYear: number;
  cvv: string;
}

export interface PaymentResponse {
  id: number;
  orderId: number;
  status: PaymentStatus;
  provider: string;
  providerRef: string;
  cardLast4: string;
  amount: number;
  currency: string;
}

export type PaymentStatus = 'SUCCESS' | 'FAILED';
