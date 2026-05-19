export interface ProfileResponse {
  id: number;
  email: string;
  fullName: string;
  enabled: boolean;
  roles: string[];
}

export interface ProfileUpdateRequest {
  fullName: string;
}

export interface AddressResponse {
  id: number;
  line1: string;
  line2: string;
  postalCode: string;
  administrativeArea: string;
  administrativeAreaCode: string;
  country: string;
  primary: boolean;
}

export interface AddressRequest {
  line1: string;
  line2?: string;
  postalCode: string;
  administrativeArea: string;
  administrativeAreaCode?: string;
  country: string;
  primary: boolean;
}
