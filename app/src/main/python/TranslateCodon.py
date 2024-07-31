#This script takes in your DNA string and translates the codons into amino
#acid codes.

#Validate that the sequence will work for the future functions.
def validate_sequence (sequence):
    if len(sequence) % 3 != 0:
        print("Incorrect number of characters. Sequence should be in a multiple of three. You have " + str(len(sequence) % 3) + " extra characters.")
        return False
    sequence.upper()
    valid_characters = {'A', 'C', 'G', 'U'}
    for char in sequence:
        if char not in valid_characters:
            print(f"Invalid character '{char}'.")
            return False
    return True

#Split the string 
def split_sequence (sequence):
    return [sequence[i:i+3] for i in range(0, len(sequence), 3)]

def translate_codon (codon):
    match codon:
        case 'GCA' | 'GCC' | 'GCG' | 'GCU':
            return "A"
        case 'CGA' | 'CGC' | 'CGG' | 'CGU' | 'AGA' | 'AGG':
            return "R"
        case 'AAU' | 'AAC':
            return "N"
        case 'GAU' | 'GAC':
            return "D"
        case 'UGU' | 'UGC':
            return "C"
        case 'CAA' | 'CAG':
            return "Q"
        case 'GAA' |'GAG':
            return "E"
        case 'GGA' | 'GGC' | 'GGU' | 'GGG':
            return "G"
        case 'CAU' | 'CAC':
            return "H"
        case 'AUA' | 'AUC' | 'AUU':
            return "I"
        case 'UUA' | 'UUG' | 'CUA' | 'CUC' | 'CUG' | 'CUU':
            return "L"
        case 'AAA' | 'AAG':
            return "K"
        case 'AUG':
            return "M"
        case 'UUU' | 'UUC':
            return "F"
        case 'CCA' | 'CCC' | 'CCG' | 'CCU':
            return "P"
        case 'AGU' | 'AGC' | 'UCA' | 'UCC' | 'UCG' | 'UCU':
            return "S"
        case 'ACA' | 'ACC' | 'ACG' | 'ACU':
            return "T"
        case 'UGG':
            return "W"
        case 'UAU' | 'UAC':
            return "Y"
        case 'GUA' | 'GUC' | 'GUG' | 'GUU':
            return "V"
        case 'UAA' | 'UAG' | 'UGA':
            return " STOP "
        case _:
            return "Invalid value. How did you do that?"

def sequence_to_codon (sequence):
    sequence.upper()
    validate_sequence(sequence)
    codons = split_sequence(sequence)
    amino_acids = []
    for codon in codons:
        amino_acid = translate_codon(codon)
        amino_acids.append(amino_acid)
    acid_string = ''.join(amino_acids)
    return acid_string
