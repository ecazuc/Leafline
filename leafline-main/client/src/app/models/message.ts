export class Message {
    constructor(public to: string, public status: string, public body:string, public id?: string, public timestamp?: number, public from?: string){}
   

}
