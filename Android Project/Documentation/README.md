# Documentation Short-hands

Noted down documentation suplements such as- use case diagram, feature list, APIs  

## Project Description

Google docs link-  
https://docs.google.com/document/d/13k6JFobf1ehFxS1JiSu4tFIxx0fdNKmcdAk6A1n1Gww/edit  

## Use case diagram

Use case diagram created using lucidchart-  
https://lucid.app/invitations/accept/6b9bb266-a47c-4394-a7a1-6f76b722b633  

![use case diagram](./use_case_diagram.png)  

## Features

### **Chef/Food Vendor side features**
  
1. Register as a chef user & login-logout  

2. <span style="color:yellow">Assign/update home location</span>  

3. Auto-detect chef user's location during registration. **[S]**

4. Set/update food offerings. Each new food offering has,
    - *Image*- Image of the food.  

    - *Name*- name of the food.

    - *Description*- Short one-two lines of description.  

    - *Items*- Ingredients used to make the food.  
  
    - <span style="color:red">*Category*- Category of food (pre-defined or user-defined?)</span>  

    - <span style="color:red">*Quantity*- User-specified quantity. (as a string?)</span>

    - *Price*- Price of single serving of the food.  

    - *Discount*- Percentage discount on the price set.  

    - <span style="color:yellow">*Offering Schedule*- Week days on which the food is likely to be offered.<span>  

5. Prompt chef user to confirm food offering scheduled for th next day. **[S]**  

6. <span style="color:yellow">Confirm scheduled food offering each day.</span>  

7. <span style="color:yellow">Create variant of an existing offering.</span>  

8. Notify chef user when an order is placed. **[S]**  

9. Keep track of chef user's delivered orders. **[S]**  

### **Customer side features**

1. Register as a chef user & login-logout.  

2. Auto-detect customer user's current location. **[S]**  

3. Sort available food offerings based on distance. **[S]**  

4. Filter vendor food offerings list shown in home screen by <span style="color:red">category<span>.  

5. <span style="color:yellow">Set a monthly meal plan based on item and/or category and/or item.</span>  

6. <span style="color:red">Order catering. (order for an event? if so, then how to implement?)<span>  

7. Order single serving food <span style="color:yellow">(based on vendor-specified quantity<span>)  

8. <span style="color:red">Confirm bKash payment by typing in alphanumeric 10 letter code. (whats the process exactly?)<span>  

9. Keep track of customer user order history. **[S]**  

10. <span style="color:red">Keep 7% of the payment for an order. (how?)<span> **[S]**
