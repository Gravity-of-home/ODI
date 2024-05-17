export interface IPaymentsHistory {
  amount?: number;
  content?: string;
  createdAt?: string;
  detailContent?: string | null;
  partyId?: number | null;
  paymentId?: number;
  pointHistoryId?: number;
  type: 'PREPAYMENT' | 'SETTLEMENT' | 'CHARGE';
}

export interface IPageInfo {
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: IPageable;
  size: number;
  sort: ISortInfo;
  totalElements: number;
  totalPages: number;
}

export interface IPageable {
  offset: number;
  pageNumber: number;
  pageSize: number;
  paged: boolean;
  unpaged: boolean;
  sort: ISortInfo;
}

export interface ISortInfo {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface IPaymentsHistoryResponse {
  content: IPaymentsHistory[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  pageable: IPageable;
  sort: ISortInfo;
}
