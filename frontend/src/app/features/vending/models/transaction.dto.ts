import { ProductDTO } from './product.dto';

export interface TransactionDTO {
  id: number;
  insertedAmount: number;
  changeGiven: number;
  items: TransactionItemDTO[];
}


export interface ChangeBreakdownDTO {
  [coinValue: string]: number;
}

export interface FinalizeTransactionResponseDTO {
  transaction: TransactionDTO;
  change: ChangeBreakdownDTO;
  status: string;
}

export interface TransactionItemDTO {
  id: number;
  productId: number;
  quantity: number;
  product: ProductDTO;
}
