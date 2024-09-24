export * from './administration.service';
import { AdministrationService } from './administration.service';
export * from './authentication.service';
import { AuthenticationService } from './authentication.service';
export * from './configuration.service';
import { ConfigurationService } from './configuration.service';
export * from './message.service';
import { MessageService } from './message.service';
export const APIS = [AdministrationService, AuthenticationService, ConfigurationService, MessageService];
