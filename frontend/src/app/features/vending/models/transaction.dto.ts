export interface ProductDTO {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

export interface TransactionDTO {
  id: number;
  insertedAmount: number;
  changeGiven: number;
  items: TransactionItemDTO[];
}

export interface TransactionItemDTO {
  id: number;
  productId: number;
  quantity: number;
  product: ProductDTO;
}
