import { AdminInfo } from './adminInfo';
export interface User {
  username: string;
  password?: string;
  rememberMe?: boolean;
  adminInfo?: AdminInfo;
  token: string;
  sessionId: any;
  authdata?: string;
}
