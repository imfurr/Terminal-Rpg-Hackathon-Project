from colorama import Fore, Style, init

class Swordsman:
    def __init__(self):
        self.name = Fore.RED + "Swordsman" + Style.RESET_ALL
        self.health = 200
        self.attack = 23
        self.defense = 13
        
    def __str__(self):
        return f"{self.name} (Health: {self.health}, Attack: {self.attack}, Defense: {self.defense})"

class Bowman:
    def __init__(self):
        self.name =  Fore.GREEN + "Bowman" + Style.RESET_ALL
        self.health = 170
        self.attack = 30
        self.defense = 13
        
    def __str__(self):
        return f"{self.name} (Health: {self.health}, Attack: {self.attack}, Defense: {self.defense})"

class Thief:
    def __init__(self):
        self.name = Fore.YELLOW + "Thief" + Style.RESET_ALL
        self.health = 150
        self.attack = 35
        self.defense = 9
        
    def __str__(self):
        return f"{self.name} (Health: {self.health}, Attack: {self.attack}, Defense: {self.defense})"

class Caster:
    def __init__(self):
        self.name = Fore.BLUE + "Caster" + Style.RESET_ALL
        self.health = 135
        self.attack = 55
        self.defense = 4

    def __str__(self):
        return f"{self.name} (Health: {self.health}, Attack: {self.attack}, Defense: {self.defense})"

def main():
    classes = [Swordsman(), Bowman(), Thief(), Caster()]
    
    print("\033[1m-----------------------------------------------------\033[0m")
    print("Pick your class! (1, 2, 3, 4) " + "\033[1m\n-----------------------------------------------------\033[0m")
    for index, character in enumerate(classes, 1):
        print(f"{index}. {character}")
    print("\033[1m-----------------------------------------------------\033[0m")
        
    while True:
        try:
            choice = int(input("Which class would you like? ")) - 1
            if choice >= 0 and choice < len(classes):
                break
            else:
                print("Invalid choice, please enter a number between 1-4.")
        except (ValueError, IndexError):
            print("Invalid input, please enter a number between 1-4.")
                
    chosen_class = classes[choice]
    
    print(f"You chose {chosen_class.name}!")
    print(f"Health: {chosen_class.health}")
    print(f"Attack: {chosen_class.attack}")
    print(f"Defense: {chosen_class.defense}" + "\033[1m\n-----------------------\033[0m")
   
    difficulty_level, difficulty_multiplier = set_difficulty()
    
    print(f"\033[1mYou have selected {difficulty_level} mode! You will take {difficulty_multiplier * 100}% damage and enemies will have {difficulty_multiplier * 100}% health!\033[0m")
    print("\033[1m-----------------------------------------------------------------------------------------------\033[0m")
    
    hotkey = choose_hotkeys()
    hotkey = hotkey.upper()
    
    print(f"\033[1mYou have chosen {hotkey} Hotkeys.\033[0m")
    print("\033[1m-----------------------------------------------------------------------------------------------\033[0m")
    
    with open("name.txt", "w") as file:
        file.write(str(chosen_class.name))
    with open("health.txt", "w") as file:
        file.write(str(chosen_class.health))
    with open("attack.txt", "w") as file:
        file.write(str(chosen_class.attack))
    with open("defense.txt", "w") as file:
        file.write(str(chosen_class.defense))
    with open("difficulty.txt", "w") as file:
        file.write(str(difficulty_multiplier))
    with open("controls.txt", "w") as file:
        file.write(str(hotkey))   
    print(Style.RESET_ALL)
    
    
def set_difficulty():
    print("\033[1mDifficulty levels: \nEasy, Medium, Hard" + "\n-----------------------\033[0m")
    while True:
        difficulty_level = input("Choose your difficulty: ").capitalize()
        if difficulty_level == "Easy":
            return difficulty_level, 0.8               
        elif difficulty_level == "Medium":
            return difficulty_level, 1                
        elif difficulty_level == "Hard":
            return difficulty_level, 1.2            
        else:
            print("\033[1mInvalid choice, please pick a difficulty(Easy, Medium, Hard).\033[0m")
def choose_hotkeys():
    print("\033[1mWould you like to use NESW(Cardinal Directions) or WASD?\033[0m")
    while True:
        hotkeys = input("Hotkeys: ")
        if hotkeys.upper() == "NESW":
            return "nesw"
        elif hotkeys.upper() == "WASD":
            return "wasd"
        else:
            print('\033[1mInvalid choice, please pick either "NESW" or "WASD"\033[0m')
if __name__ == "__main__":
    main()
    